package me.pcrunn.data;

import com.rethinkdb.net.Connection;
import lombok.Getter;
import me.pcrunn.data.rank.RankService;
import me.pcrunn.data.user.UserService;
import me.pcrunn.data.user.listener.UserListener;
import org.bukkit.plugin.java.JavaPlugin;

import static com.rethinkdb.RethinkDB.r;

public class DataPlugin extends JavaPlugin {

    @Getter
    private static DataPlugin dataPlugin;

    @Getter
    private Connection connection;

    @Getter
    private UserService userService;

    @Getter
    private RankService rankService;

    @Override
    public void onEnable() {
        dataPlugin = this;

        /* connect to the database */
        this.connection = r.connection()
                .hostname("127.0.0.1")
                .port(28015)
                .db("carly")
                .connect();

        /* create tables if they don't exist */
        try {
            r.dbCreate("carly").run(this.connection);
            r.tableCreate("users").run(this.connection);
            r.tableCreate("ranks").run(this.connection);
        } catch (Exception exception) {
            /* no one asked */
        }

        /* initialize the user service */
        this.userService = new UserService(this);

        /* initialize the rank service */
        this.rankService = new RankService(this);

        /* register listeners */
        this.getServer().getPluginManager().registerEvents(new UserListener(this), this);
    }

}
