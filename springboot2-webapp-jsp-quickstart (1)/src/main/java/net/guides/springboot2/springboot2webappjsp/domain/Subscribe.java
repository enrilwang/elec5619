package net.guides.springboot2.springboot2webappjsp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Entity
@Table(name = "subscribe")
public class Subscribe {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int subscribeId;
    private boolean activated;
    private int creatorId;
    private int paymentsSerialId;//??
    private int subscribeType;//only 0, 1, 2
    private int subscriptionTypeId;
    private int userId;
    private String timeStart;
    private String timeEnd;
}