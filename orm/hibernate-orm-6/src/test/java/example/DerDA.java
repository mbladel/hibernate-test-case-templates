package example;


import jakarta.persistence.Embeddable;

/*
 * Created on 03/12/2024 by Paul Harrison (paul.harrison@manchester.ac.uk).
 */
@Embeddable
public class DerDA extends BaseD {
  public DerDA(String a, String bprop) {
    super(bprop);
    this.a = a;
  }

  public String a;

  public DerDA() {
  }
}
