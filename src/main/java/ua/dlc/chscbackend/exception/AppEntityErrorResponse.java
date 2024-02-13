package ua.dlc.chscbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppEntityErrorResponse {
    private int status;
    private String exception;
    private String message;
    private LocalDateTime timeStamp;

}
