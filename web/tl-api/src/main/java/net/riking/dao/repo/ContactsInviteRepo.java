package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ContactsInvite;

@Repository
public interface ContactsInviteRepo
		extends JpaRepository<ContactsInvite, String>, JpaSpecificationExecutor<ContactsInvite> {

	@Query("select phone from ContactsInvite where userId =?1")
	List<String> findByUserId(String userId);

	@Query("select phone from ContactsInvite where userId =?1 and phone = ?2")
	ContactsInvite findByOne(String userId, String phone);
}
