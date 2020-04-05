package me.pcrunn.data.user;

import me.pcrunn.data.DataPlugin;
import me.pcrunn.data.rank.Rank;
import me.pcrunn.data.user.statistics.UserStatistics;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

/**
 * Handles the creation and loading of users
 */
public class UserFactory {

    /**
     * Loads a user from a map
     *
     * @return the user
     */
    public User fromMap(Map<String, Object> map) {
        /* get uuid from map */
        UUID uuid = UUID.fromString(map.get("uuid").toString());
        assert uuid != null;

        /* get name from map */
        String name = map.get("name").toString();
        assert name != null;

        /* get rank name from map */
        String rankName = map.get("rank").toString();
        assert rankName != null;

        /* parse the rank and get it as a rank object */
        Rank rank = DataPlugin.getDataPlugin().getRankService().find(rankName);
        assert rank != null;

        /* get statistics map from map */
        Map<String, Object> statisticsMap = (Map<String, Object>) map.get("statistics");
        assert statisticsMap != null;

        /* get elo from statistics map */
        long elo = (Long) statisticsMap.get("elo");
        assert (Object) elo != null;

        /* get playtime */
        long playtime = (Long) map.get("playtime");
        assert (Object) playtime != null;

        /* create statistics */
        UserStatistics statistics = UserStatistics.builder()
                .build();

        /* create user */
        User user = new User(uuid);

        user.setName(name);
        user.setRank(rank);
        user.setStatistics(statistics);

        return user;
    }

    /**
     * Creates a default user
     *
     * @return the user
     */
    public User createDefaultUser(UUID uuid) {
        /* create the user */
        User user = new User(uuid);

        /* create the statistics and initialize everything to the defaults */
        UserStatistics statistics = UserStatistics.builder()
                .elo(0)
                .build();

        /* initialize user */
        user.setStatistics(statistics);
        user.setPlaytime(0);
        user.setRank(DataPlugin.getDataPlugin().getRankService().getDefaultRank());
        user.setName(Bukkit.getOfflinePlayer(uuid).getName());

        return user;
    }

}
