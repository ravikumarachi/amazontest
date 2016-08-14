package CustomCucumberRunner;

import CustomAnnotations.AfterAll;
import CustomAnnotations.BeforeAll;
import cucumber.api.junit.Cucumber;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ExtendedCucumberRunner extends Runner {
    private Class aClass;
    private Cucumber cucumber;

    public ExtendedCucumberRunner(Class aClass) throws Exception {
        this.aClass = aClass;
        cucumber = new Cucumber(aClass);
    }

    @Override
    public Description getDescription() {
        return cucumber.getDescription();
    }

    private void runPredefinedMethods(Class annotation) throws Exception {
        if (!annotation.isAnnotation()) {
            return;
        }
        Method[] methodList = this.aClass.getMethods();
        for (Method method : methodList) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation item : annotations) {
                if (item.annotationType().equals(annotation)) {
                    method.invoke(null);
                    break;
                }
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            runPredefinedMethods(BeforeAll.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cucumber.run(notifier);
        try {
            runPredefinedMethods(AfterAll.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
