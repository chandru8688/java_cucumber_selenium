package newsvalidation.utilities.support;

import newsvalidation.factory.DriverFactory;

public class CommonFunctions extends DriverFactory {
    public final int VERY_SHORT = 1000;
    public final int SHORT = 3000;
    public final int MEDIUM = 5000;
    public final int LONG = 10000;
    public final int VERY_LONG = 30000;

    public void threadWaitForVeryShortTime() {
        try{
            Thread.sleep(VERY_SHORT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void threadWaitForShortTime() {
        try{
            Thread.sleep(SHORT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void threadWaitForMediumTime() {
        try{
            Thread.sleep(MEDIUM);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void threadWaitForLongTime() {
        try{
            Thread.sleep(LONG);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void threadWaitForVeryLongTime() {
        try{
            Thread.sleep(VERY_LONG);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
