package com.zpg.trumptweets.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A User_preferences.
 */
@Entity
@Table(name = "user_preferences")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQuery(name="findUsersForCategory", query="select u.user from User_preferences u where :category NOT IN elements(u.excluded_categories) and u.user.activated is true")
public class User_preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_preferences_excluded_categories",
               joinColumns = @JoinColumn(name="user_preferences_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="excluded_categories_id", referencedColumnName="id"))
    private Set<Category> excluded_categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public User_preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getExcluded_categories() {
        return excluded_categories;
    }

    public User_preferences excluded_categories(Set<Category> categories) {
        this.excluded_categories = categories;
        return this;
    }

    public User_preferences addExcluded_categories(Category category) {
        this.excluded_categories.add(category);
        category.getUser_exclusions().add(this);
        return this;
    }

    public User_preferences removeExcluded_categories(Category category) {
        this.excluded_categories.remove(category);
        category.getUser_exclusions().remove(this);
        return this;
    }

    public void setExcluded_categories(Set<Category> categories) {
        this.excluded_categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User_preferences user_preferences = (User_preferences) o;
        if (user_preferences.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, user_preferences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User_preferences{" +
            "id=" + id +
            '}';
    }
}
