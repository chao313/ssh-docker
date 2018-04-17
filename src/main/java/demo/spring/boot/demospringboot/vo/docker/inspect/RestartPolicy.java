/**
  * Copyright 2018 bejson.com 
  */
package demo.spring.boot.demospringboot.vo.docker.inspect;

/* created by chao*/
public class RestartPolicy {

    private int MaximumRetryCount;
    private String Name;
    public void setMaximumRetryCount(int MaximumRetryCount) {
         this.MaximumRetryCount = MaximumRetryCount;
     }
     public int getMaximumRetryCount() {
         return MaximumRetryCount;
     }

    public void setName(String Name) {
         this.Name = Name;
     }
     public String getName() {
         return Name;
     }

}