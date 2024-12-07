package maiawein_CSCI201_Final;

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

	// Log out message class.
	public static class LogOutMessage
	{
        public int userID; // The user ID requesting the log-out

		public LogOutMessage(int userID)
		{
			this.userID = userID;
		}
	}

    // Delete account message class.
    public static class DeleteAccountMessage {
        public int userID; // The user ID of the account to be deleted

        public DeleteAccountMessage(int userID) {
            this.userID = userID;
        }
    }
}
