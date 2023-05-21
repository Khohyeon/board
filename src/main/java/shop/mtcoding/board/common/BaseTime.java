package shop.mtcoding.board.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/* JPA Entity 클래스들이 BaseTime 상속할 경우 필드들(createdDate, modifiedDate)도 칼럼으로 인식 */
/* BaseTime 클래스에 Auditing 기능을 포함 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void changeCreatedDate(LocalDateTime now) {
        createdDate = Objects.requireNonNullElseGet(now, LocalDateTime::now);
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}