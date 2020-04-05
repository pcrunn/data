package me.pcrunn.data.rank;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handles the creation and loading of ranks
 */
public class RankFactory {

    public Rank fromMap(Map<String, Object> map) {
        /* get name from map */
        String name = map.get("name").toString();
        assert name != null;

        /* get color from map */
        ChatColor color = ChatColor.valueOf(map.get("color").toString());
        assert color != null;

        /* get permissions from map */
        List<String> permissions = (List<String>) map.get("permissions");
        assert permissions != null;

        /* get default from map */
        boolean default$ = (boolean) map.get("default$");
        assert (Object) default$ != null;

        /* create rank */
        Rank rank = new Rank(name);

        rank.setColor(color);
        rank.setPermissions(permissions);
        rank.setDefault$(default$);

        return rank;
    }

    /**
     * Creates a default rank
     *
     * @return the rank
     */
    public Rank createDefaultRank(String name) {
        /* create a rank with the name provided */
        Rank rank = new Rank(name);

        /* initialize the rank */
        rank.setColor(ChatColor.GREEN);
        rank.setDefault$(false);
        rank.setPermissions(Arrays.asList(""));

        return rank;
    }

}
