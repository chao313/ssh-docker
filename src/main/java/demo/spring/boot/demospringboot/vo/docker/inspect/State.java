/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/* created by chao*/
@Data
@ToString
public class State {
    private String Status;
    private boolean Restarting;
    private boolean Dead;
    private int ExitCode;
    private boolean Running;
    private String Error;
    private String FinishedAt;
    private boolean OOMKilled;
    private int Pid;
    private String StartedAt;
    private boolean Paused;

}