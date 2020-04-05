package me.pcrunn.data.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.pcrunn.data.DataPlugin;
import me.pcrunn.data.rank.Rank;
import me.pcrunn.data.user.statistics.UserStatistics;

import java.util.UUID;

@ToString
@RequiredArgsConstructor
public class User {

    @NonNull
    @Getter
    @Setter
    private UUID uuid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private UserStatistics statistics;

    @Getter
    @Setter
    private long playtime;

    @Getter
    @Setter
    @JsonIgnore
    private Rank rank;

    @Getter
    @Setter
    private long joined;

    /**
     * Get the rank of the user
     *
     * @return the user
     */
    @JsonProperty("rank")
    public String getRankString() {
        return this.rank.getName();
    }

}
