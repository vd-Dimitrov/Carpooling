package org.example.carpooling.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_id")
    private int optionId;

    @Column(name = "option", nullable = false)
    private String optionType;

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String option) {
        this.optionType = option;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return optionId == option.optionId;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(optionId);
    }
}
