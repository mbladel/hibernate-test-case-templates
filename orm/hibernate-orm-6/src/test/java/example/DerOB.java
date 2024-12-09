package example;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

/*
 * Created on 03/12/2024 by Paul Harrison (paul.harrison@manchester.ac.uk).
 */
@Entity
public class DerOB extends BaseO {
   public DerOB(DerDB derdb) {
      this.derdb = derdb;
   }

   @Embedded
   BaseD derdb;

   public DerOB() {

   }

   public DerDB derdb() {
      return (DerDB) derdb;
   }
}
