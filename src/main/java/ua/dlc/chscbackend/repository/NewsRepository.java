package ua.dlc.chscbackend.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ua.dlc.chscbackend.model.News;

@RepositoryRestResource
public interface NewsRepository extends JpaRepository<News, Long> {
}
