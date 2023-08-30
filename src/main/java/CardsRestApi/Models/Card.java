package CardsRestApi.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Tbl_Cards")
public class Card {

    @Id
    @SequenceGenerator(
            name = "Card_Sequence",
            sequenceName = "User_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Card_Sequence"
    )
    private Long id;
    private String cardNumber;
    @NotBlank(message = "Card Name Cannot Be Blank")
    private String name;
    private String description;
    @Pattern(regexp = "^#([0-9a-fA-F]{6})$", message = "Color must be a 6-character hexadecimal code starting with #")
    private String color;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate createdDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
