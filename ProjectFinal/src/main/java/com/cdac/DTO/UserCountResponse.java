package com.cdac.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserCountResponse {
	
	    private LocalDateTime timeStamp;
	    private String message;
	    private Long totalUsers;
	    
	    public UserCountResponse (String message, Long totalUsers) {
			super();
			this.message = message;
			this.totalUsers = totalUsers;
			this.timeStamp=LocalDateTime.now();
		}

}
