package koscom.team6.domain.match.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @Nullable
    private String imageUrl1;

    @Column
    @Nullable
    private String imageUrl2;

    @Column
    @Nullable
    private String imageUrl3;

    @Column
    @Nullable
    private String imageUrl4;

    @Column
    @Nullable
    private String tag1;

    @Column
    @Nullable
    private String tag2;

    @Column
    @Nullable
    private String tag3;

    @Builder
    private Match(String title, String content, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String tag1, String tag2, String tag3) {
        this.title = title;
        this.content = content;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
    }

    public static Match of(String title, String content, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4, String tag1, String tag2, String tag3) {
        return Match.builder()
                .title(title)
                .content(content)
                .imageUrl1(imageUrl1)
                .imageUrl2(imageUrl2)
                .imageUrl3(imageUrl3)
                .imageUrl4(imageUrl4)
                .tag1(tag1)
                .tag2(tag2)
                .tag3(tag3)
                .build();
    }
}
