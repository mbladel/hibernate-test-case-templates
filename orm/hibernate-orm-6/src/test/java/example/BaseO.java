package example;


/*
 * Created on 03/12/2024 by Paul Harrison (paul.harrison@manchester.ac.uk).
 */

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseO {
   @Id
   @GeneratedValue
   private Integer id;

   public Integer getId() {
      return id;
   }
}
