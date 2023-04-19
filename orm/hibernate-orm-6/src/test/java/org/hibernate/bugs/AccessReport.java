package org.hibernate.bugs;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;

@MappedSuperclass
public abstract class AccessReport<O> extends GenericObject<EmbeddableKey<O, Report>> {

	@Entity
	@Table( name = "CS_USER_REPORT" )
	public static class UserReport extends AccessReport<User> {

		@Override
		@EmbeddedId
		@AssociationOverrides( {
				@AssociationOverride( name = "owner", joinColumns = {
						@JoinColumn( name = "USER_ID", nullable = false, updatable = false, insertable = false )
				} ),
				@AssociationOverride( name = "entity", joinColumns = {
						@JoinColumn( name = "REPORT_ID", nullable = false, updatable = false, insertable = false )
				} )
		} )
		public EmbeddableKey<User, Report> getId() {
			return super.getId();
		}

		@Override
		public void setId(EmbeddableKey<User, Report> key) {
			super.setId( key );
		}

	}

	@Entity
	@Table( name = "CS_USER_GROUP_REPORT" )
	public static class UserGroupReport extends AccessReport<UserGroup> {

		@Override
		@EmbeddedId
		@AssociationOverrides( {
				@AssociationOverride( name = "owner", joinColumns = {
						@JoinColumn( name = "USER_GROUP_ID", nullable = false, updatable = false, insertable = false )
				} ),
				@AssociationOverride( name = "entity", joinColumns = {
						@JoinColumn( name = "REPORT_ID", nullable = false, updatable = false, insertable = false )
				} )
		} )
		public EmbeddableKey<UserGroup, Report> getId() {
			return super.getId();
		}

		@Override
		public void setId(EmbeddableKey<UserGroup, Report> key) {
			super.setId( key );
		}

	}
}
