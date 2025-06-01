package ro.mpp;

public abstract class AbstractService<ID, E extends Entity<ID>, R extends IRepository<ID, E>> implements IService<ID, E> {
    private final R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    public R getRepository() {
        return repository;
    }

    @Override
    public void save(E entity) throws ValidationException {
        repository.save(entity);
    }

    @Override
    public void update(E entity) throws ValidationException {
        repository.update(entity);
    }

    @Override
    public void delete(ID id) throws ValidationException {
        repository.delete(id);
    }

    @Override
    public E findById(ID id) throws ValidationException {
        return repository.findById(id);
    }

    @Override
    public Iterable<E> findAll() throws ValidationException {
        return repository.findAll();
    }
}
