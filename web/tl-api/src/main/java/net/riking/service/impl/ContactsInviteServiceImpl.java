package net.riking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.riking.dao.repo.ContactsInviteRepo;
import net.riking.entity.model.ContactsInvite;
import net.riking.entity.model.MQOptCommon;
import net.riking.entity.params.UserParams;
import net.riking.service.ContactsInviteService;
import net.riking.util.Utils;

@Service("contactsInviteService")
@Transactional
public class ContactsInviteServiceImpl implements ContactsInviteService {
	private static final Logger logger = LogManager.getLogger(ContactsInviteService.class);

	@Autowired
	ContactsInviteRepo contactsInviteRepo;

	@Override
	public void contactsInvite(MQOptCommon optCommon) throws IllegalArgumentException, IllegalAccessException {
		UserParams userParams = new UserParams();
		userParams = (UserParams) Utils.fromObjToObjValue(optCommon, userParams);
		ContactsInvite contactsInvite = contactsInviteRepo.findByOne(userParams.getUserId(), userParams.getPhone());
		if (contactsInvite == null) {
			contactsInvite = new ContactsInvite();
			contactsInvite.setPhone(userParams.getPhone());
			contactsInvite.setUserId(userParams.getUserId());
			contactsInviteRepo.save(contactsInvite);
		}
	}

}
