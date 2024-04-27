package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Collection<Meeting> getAll(String key, String sortBy, String sortOrder) {
		String hql = "FROM Meeting WHERE title LIKE :keyValue";
		if(sortBy.equals("id")){
			hql += " ORDER BY " +sortBy;
			if(sortOrder.equals("ASC") || sortOrder.equals("DESC")){
				hql += " " + sortOrder;
			}
		}
		Query query = session.createQuery(hql);
		query.setParameter("keyValue", "%" + key + "%");
		return query.list();
	}

	public Meeting findById(long id) {
		return session.get(Meeting.class, id);
	}

}
