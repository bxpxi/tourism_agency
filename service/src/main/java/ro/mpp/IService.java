package ro.mpp;

public interface IService<ID, E extends Entity<ID>> {
    E findById(ID id) throws ValidationException;
    Iterable<E> findAll() throws ValidationException;
    void save(E entity) throws ValidationException;
    void delete(ID id) throws ValidationException;
    void update(E entity) throws ValidationException;
}