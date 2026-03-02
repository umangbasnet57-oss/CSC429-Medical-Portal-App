package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class UserRepositoryImpl implements UserRepository{

    public UserRepositoryImpl(){}
    /**
     *
     */
    @Override
    public void flush() {

    }

    /**
     * @param entity entity to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    /**
     * @param entities entities to be saved. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param entities entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    /**
     * @param integers the ids of the entities to be deleted. Must not be {@literal null}.
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    /**
     *
     */
    @Override
    public void deleteAllInBatch() {

    }

    /**
     * @param integer must not be {@literal null}.
     * @return
     */
    @Override
    public User getOne(Integer integer) {
        return null;
    }

    /**
     * @param integer must not be {@literal null}.
     * @return
     */
    @Override
    public User getById(Integer integer) {
        return null;
    }

    /**
     * @param integer must not be {@literal null}.
     * @return
     */
    @Override
    public User getReferenceById(Integer integer) {
        return null;
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    /**
     * @param example must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        return List.of();
    }

    /**
     * @param example must not be {@literal null}.
     * @param sort    the {@link Sort} specification to sort the results by, may be {@link Sort#unsorted()}, must not be
     *                {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    /**
     * @param example  must not be {@literal null}.
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    /**
     * @param example the {@link Example} to count instances for. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    /**
     * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    /**
     * @param example       must not be {@literal null}.
     * @param queryFunction the query function defining projection, sorting, and the result type
     * @param <S>
     * @param <R>
     * @return
     */
    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * @param entity must not be {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> S save(S entity) {
        return null;
    }

    /**
     * @param entities must not be {@literal null} nor must it contain {@literal null}.
     * @param <S>
     * @return
     */
    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    /**
     * @param integer must not be {@literal null}.
     * @return
     */
    @Override
    public Optional<User> findById(Integer integer) {
        return Optional.empty();
    }

    /**
     * @param integer must not be {@literal null}.
     * @return
     */
    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<User> findAll() {
        return List.of();
    }

    /**
     * @param integers must not be {@literal null} nor contain any {@literal null} values.
     * @return
     */
    @Override
    public List<User> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    /**
     * @return
     */
    @Override
    public long count() {
        return 0;
    }

    /**
     * @param integer must not be {@literal null}.
     */
    @Override
    public void deleteById(Integer integer) {

    }

    /**
     * @param entity must not be {@literal null}.
     */
    @Override
    public void delete(User entity) {

    }

    /**
     * @param integers must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    /**
     * @param entities must not be {@literal null}. Must not contain {@literal null} elements.
     */
    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    /**
     *
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param sort the {@link Sort} specification to sort the results by, can be {@link Sort#unsorted()}, must not be
     *             {@literal null}.
     * @return
     */
    @Override
    public List<User> findAll(Sort sort) {
        return List.of();
    }

    /**
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be
     *                 {@literal null}.
     * @return
     */
    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }
}
