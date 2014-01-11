package upmc.stl.aar.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMFService {
	/**
	 * Singleton for EntityManagerFactory
	 */
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactions-optional");

	private EMFService() {
	}

	/**
	 * Retrieves singleton instance.
	 * @return
	 */
	public static EntityManagerFactory get() {
		return emfInstance;
	}
}