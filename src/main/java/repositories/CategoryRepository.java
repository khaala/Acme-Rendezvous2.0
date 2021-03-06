
package repositories;

import java.util.Collection;

import domain.Rendezvous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select r from Rendezvous r join r.servises s order by s.category.name ")
	Collection<Rendezvous> findAllRendezvousByCategoryId(int categoryId);

	@Query("select c from Category c where c.parentCategory.id = ?1")
	Collection<Category> findCategoryChildrenId(int categoryId);

	@Query("select case when (count(son.name) > 0) then true else false end from Category parent join parent.childrenCategories son where (son.name = ?1 and parent.id = ?2) or parent.name = ?1")
	Boolean existsThisCategoryName(String nameCheck, Integer parentId);

	@Query("select s.rendezvouses from Servise s where s.category.id = ?1")
	Collection<Rendezvous> findAllRendezvousByCategoryId2(int categoryId);

	@Query("select c from Category c where c.name='CATEGORY'")
    Category findCategoryRaiz();


    // Queries Nivel B- 11.1.a
	//Ratio por categoría
	//select count(c)*1.0 / (select count(c1)*1.0 from Category c1) from Category c join c.servises s group by s;
}
