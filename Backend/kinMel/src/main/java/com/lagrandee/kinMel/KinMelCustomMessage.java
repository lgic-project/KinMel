package com.lagrandee.kinMel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KinMelCustomMessage {
    private int status;
    private String message;
    private long timeStamp;

    public KinMelCustomMessage(int status, long timeStamp) {
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public KinMelCustomMessage(String message, long timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
