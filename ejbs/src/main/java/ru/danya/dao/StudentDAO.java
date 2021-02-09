package ru.danya.dao;

import ru.danya.entity.Student;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentDAO {

    public boolean addEntity(Student student);

    public boolean deleteEntity(long id);

    public List<Student> getAllEntities();
}
