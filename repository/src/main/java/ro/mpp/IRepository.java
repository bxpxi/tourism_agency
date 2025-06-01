package ro.mpp;

public interface IRepository<ID, E> {
    E findById(ID id);
    Iterable<E> findAll();
    void save(E entity);
    void delete(ID id);
    void update(E entity);
}