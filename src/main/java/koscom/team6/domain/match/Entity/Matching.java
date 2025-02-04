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
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String imageDescription1;

    @Column
    @Nullable
    private String imageUrl2;

    @Column
    @Nullable
    private String imageDescription2;

    @Column
    @Nullable
    private String imageUrl3;

    @Column
    @Nullable
    private String imageDescription3;
//
//    @Column
//    @Nullable
//    private String imageUrl4;
//
//    @Column
//    @Nullable
//    private String imageDescription4;

    @Column
    @Nullable
    private String tag1;

    @Column
    @Nullable
    private String tag2;

    @Column
    @Nullable
    private String tag3;

    @Column
    @Nullable
    private String reference1;

    @Column
    @Nullable
    private String referenceLink1;

    @Column
    @Nullable
    private String reference2;

    @Column
    @Nullable
    private String referenceLink2;

    @Column
    @Nullable
    private String reference3;

    @Column
    @Nullable
    private String referenceLink3;

    @Builder
    private Matching(String title, String content, String imageUrl1, String imageDescription1, String imageUrl2, String imageDescription2, String imageUrl3, String imageDescription3, String tag1, String tag2, String tag3, String reference1, String referenceLink1, String reference2, String referenceLink2, String reference3, String referenceLink3) {
        this.title = title;
        this.content = content;
        this.imageUrl1 = imageUrl1;
        this.imageDescription1 = imageDescription1;
        this.imageUrl2 = imageUrl2;
        this.imageDescription2 = imageDescription2;
        this.imageUrl3 = imageUrl3;
        this.imageDescription3 = imageDescription3;
//        this.imageUrl4 = imageUrl4;
//        this.imageDescription4 = imageDescription4;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.reference1 = reference1;
        this.referenceLink1 = referenceLink1;
        this.reference2 = reference2;
        this.referenceLink2 = referenceLink2;
        this.reference3 = reference3;
        this.referenceLink3 = referenceLink3;
    }

    public static Matching of(String title, String content, String imageUrl1, String imageDescription1, String imageUrl2, String imageDescription2, String imageUrl3, String imageDescription3, String tag1, String tag2, String tag3, String reference1, String referenceLink1, String reference2, String referenceLink2, String reference3, String referenceLink3) {
        return Matching.builder()
                .title(title)
                .content(content)
                .imageUrl1(imageUrl1)
                .imageDescription1(imageDescription1)
                .imageUrl2(imageUrl2)
                .imageDescription2(imageDescription2)
                .imageUrl3(imageUrl3)
                .imageDescription3(imageDescription3)
//                .imageUrl4(imageUrl4)
//                .imageDescription4(imageDescription4)
                .tag1(tag1)
                .tag2(tag2)
                .tag3(tag3)
                .reference1(reference1)
                .referenceLink1(referenceLink1)
                .reference2(reference2)
                .referenceLink2(referenceLink2)
                .reference3(reference3)
                .referenceLink3(referenceLink3)
                .build();
    }
}
