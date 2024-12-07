/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.exception;

/**
 *
 * @author sarobidy
 */
public class CoupureException extends Exception {
    
    int minute;
    int hour;

          public int getHour() {
                    return hour;
          }

          public void setHour(int hour) {
                    this.hour = hour;
          }
    
    

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if( minute == 60 ){
                  this.minute = 0;
                  this.setHour( this.getHour() + 1 );
                  return;
        }
        this.minute = minute;
    }
    
    public CoupureException() {
    }
    
    public CoupureException(String message) {
        super(message);
    }
    
}
