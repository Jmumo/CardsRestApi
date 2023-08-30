package CardsRestApi.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_ROLE("admin"),
    MEMBER_ROLE("member")
    ;

    @Getter
    private final String permission;

}