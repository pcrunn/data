package me.pcrunn.data.rank;

import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Result;
import lombok.Getter;
import me.pcrunn.data.DataPlugin;

import java.util.HashSet;
import java.util.Map;

import static com.rethinkdb.RethinkDB.r;

/**
 * Handles loading and saving of services
 */
@Getter
public class RankService {

    private final DataPlugin plugin;
    private final RankFactory factory;
    private final HashSet<Rank> ranks;

    /**
     * Creates the {@link RankService}
     *
     * @param plugin the plugin
     */
    public RankService(DataPlugin plugin) {
        this.plugin = plugin;
        this.factory = new RankFactory();
        this.ranks = new HashSet<>();

        this.loadRanks();
    }

    /**
     * Loads all the rank (they should be loaded at all time but
     * we still have a cache for security purposes)
     */
    public void loadRanks() {
        r
                .table("ranks")
                .getAll()
                .run(this.plugin.getConnection())
                .forEach(document -> {
                    Rank rank = this.factory.fromMap((Map<String, Object>) document);

                    this.ranks.add(rank);
                    this.save(rank);
                });
    }

    /**
     * Finds a rank in the store
     *
     * @param name the name of the rank
     * @return the rank
     */
    public Rank find(String name) {
        Rank found;

        /* try to find in cache */
        found = this.ranks
                .stream()
                .filter(rank -> rank.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        /* check if it was found, if so, return it */
        if(found != null) return found;

        /* if it was not found, try to find in the database */
        Result<Object> result = r
                .table("ranks")
                .filter(document -> document.g("name").eq(name))
                .run(this.plugin.getConnection());

        /* check if it was found, if so, return it */
        if(result.hasNext()) {
            found = this.factory.fromMap((Map<String, Object>) result.next());
            this.ranks.add(found);
            return found;
        }

        return found;
    }

    /**
     * Saves a rank to the database
     *
     * @param rank the rank
     */
    public void save(Rank rank) {
        /* check if the rank is already in the database */
        boolean found = r
                .table("ranks")
                .filter(document -> document.g("name").eq(rank.getName()))
                .run(this.plugin.getConnection())
                .hasNext();

        /* get the ranks table so we don't repeat any code */
        Table table = r
                .table("ranks");

        if (found) {
            /* if the rank is already in the database, update the document */
            table
                    .update(rank)
                    .run(this.plugin.getConnection());
        } else {
            /* if the rank is not in the database, insert the document */
            table
                    .insert(rank)
                    .run(this.plugin.getConnection());
        }


    }


    /**
     * Gets the default rank
     *
     * @return the rank
     */
    public Rank getDefaultRank() {
        return this.ranks
                .stream()
                .filter(Rank::isDefault$)
                .findFirst()
                .orElseGet(() -> {
                    Rank rank = this.factory.createDefaultRank("default");

                    this.ranks.add(rank);
                    this.save(rank);

                    return rank;
                });
    }

}
