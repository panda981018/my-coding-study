package hellojpa;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    // GeneratedValue
    // MySQL 같은 경우는 IDENTITY
    // Oracle 같은 경우는 SEQUENCE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name")
    private String username;

    public Member() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
