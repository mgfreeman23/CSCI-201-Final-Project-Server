//package maiawein_CSCI201_Final;
package com.collegeapp;
public class Settings
{

	// Status message class for success or error responses.
	public static class StatusMessage
	{
		public boolean success;
		public String errorCause;

		public StatusMessage(boolean success, String errorCause)
		{
			this.success = success;
			this.errorCause = errorCause;
		}
	}

	// Change password message class.
	public static class ChangePasswordMessage
	{
		public int userID; // The user ID requesting the password change
		public String newPassword; // The new password provided by the user

		public ChangePasswordMessage(int userID, String newPassword)
		{
			this.userID = userID;
			this.newPassword = newPassword;
		}
	}
}
