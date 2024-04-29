package com.example.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
// 인덱스란 추가적인 쓰기작업과 저장공간을 활용하여 테이블의 검색속도를 향상시키는 자료구조
// 책의 색인과 같다.
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 1000) private String content;
    @Setter private String hashtag;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("id")
    @ToString.Exclude
    private final Set<ArticleComment> articleComments = new HashSet<>();


    /* createdAt, CreatedBy, LastModifiedDate, LastModifiedBy
:  데이터가 생성되거나, 수정될 때, 생성 및 수정한 사용자의 id를 넣어줌.
* https://velog.io/@minji104/CreatedBy-UpdatedBy-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84
* */
    @CreatedDate @Column(nullable = false)private LocalDateTime createdAt; //
    @CreatedDate @Column(nullable = false, length = 100) private String createdBy;
    @LastModifiedDate @Column(nullable = false)private LocalDateTime modifiedAt;
    @LastModifiedBy @Column(nullable = false, length = 100)private String modifiedBy;

    protected Article(){
    }

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public  static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
