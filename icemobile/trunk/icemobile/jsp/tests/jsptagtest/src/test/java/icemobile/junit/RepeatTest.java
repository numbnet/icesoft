package icemobile.junit;



import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runner.notification.RunNotifier;


public class RepeatTest extends BlockJUnit4ClassRunner {

 private int repeats;

  public RepeatTest(Class<?> classInst) throws InitializationError {
    super(classInst);
    Repeat r = classInst.getAnnotation(Repeat.class);
    if (r == null) {
        throw new InitializationError("A @Repeat annotation amount must also be provided when used, for example @Repeat(5) to repeat 5 times");
    }
    repeats = r.value();
  }

  @Override
  protected void runChild(FrameworkMethod method, RunNotifier notifier) {
    for (int i = 0; i < repeats; i++) {
        super.runChild(method, notifier);
    }
  }

  @Override
  public int testCount() {
    return repeats * super.testCount();
  }
}
