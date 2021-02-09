package ru.danya.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "students")
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "student_name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "academic_group")
    private String group;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    // id в таблице ставим на автоинкремент, поэтому его нет смысла прописывать в конструкторе;
    // при получении записи из БД проще вставить его сеттером.
    public Student(String name, String surname, String group, Date dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.dateOfBirth = dateOfBirth;
    }

    public Student() {}

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGroup() {
        return group;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return String.format(
                "%-5d %-24s %-24s %-16s %-12s",
                id,
                name,
                surname,
                group,
                dateOfBirth.toString()
        );
    }

    public static Student castFromObject(Object o) {
        return (Student) o;
    }
}