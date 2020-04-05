package me.pcrunn.data.rank;

import lombok.*;
import org.bukkit.ChatColor;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Rank {

    @NonNull
    private String name;

    private ChatColor color;
    private List<String> permissions;
    private boolean default$;

    public boolean isOp() {
        return this.permissions.contains("*");
    }

}
