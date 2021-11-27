package src.argon.argon.security.models;

import com.sun.istack.NotNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String authority;

    @Column(name = "authority_description")
    private String authorityDescription;

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getAuthorityDescription() {
        return authorityDescription;
    }
}
