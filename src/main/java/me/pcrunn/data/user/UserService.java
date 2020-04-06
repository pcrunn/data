package me.pcrunn.data.user;

import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.net.Result;
import lombok.Getter;
import me.pcrunn.data.DataPlugin;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

import static com.rethinkdb.RethinkDB.r;

/**
 * Handles users and database actions
 */
@Getter
public class UserService {

    private final DataPlugin plugin;
    private final UserFactory factory;
    private final Set<User> users;

    /**
     * Creates the {@link UserService}
     *
     * @param plugin the plugin
     */
    public UserService(DataPlugin plugin) {
        this.plugin = plugin;
        this.factory = new UserFactory();
        this.users = new HashSet<>();
    }

    /**
     * Finds a user in the store
     *
     * @param uuid the user's uuid
     * @return the user
     */
    public User find(UUID uuid) {
        User found;

        Bukkit.broadcastMessage(this.debugUsers().toString());

        /* try to find in cache */
        found = this.users
                .stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);

        /* check if it was found */
        /* if so, return it */
        if (found != null) return found;

        /* if it was not found */
        /* try to find in the database */
        Result<Object> result = r
                .table("users")
                .filter(document -> document.g("uuid").eq(uuid.toString()))
                .run(this.plugin.getConnection());

        /* check if it was found */
        /* if so, load it and return it */
        if (result.hasNext()) {
            found = this.factory.fromMap((Map<String, Object>) result.next());
            this.users.add(found);
            return found;
        }

        /* if it was not found in the database */
        /* create it and add it to the database */
        found = this.factory.createDefaultUser(uuid);
        this.save(found);
        this.users.add(found);

        return found;
    }

    /**
     * Saves a user object
     *
     * @param user the user object
     */
    public void save(User user) {
        /* check if the user is already in the database */
//        boolean found = r
//                .table("users")
//                .filter(document -> document.g("uuid").eq(user.getUuid().toString()))
//                .run(this.plugin.getConnection())
//                .hasNext();

        /* get the users table so we don't repeat any code */
        Table table = r
                .table("users");

//        if (found) {
            /* if the user is already in the database, update the document */
            table
                    .update(user)
                    .run(this.plugin.getConnection());
//        } else {
//            /* if the user is not in the database, insert the document */
//            table
//                    .insert(user)
//                    .run(this.plugin.getConnection());
//        }


    }

    public List<String> debugUsers() {
        return this.users.stream().map(user -> user.getName()).collect(Collectors.toList());
    }

}
