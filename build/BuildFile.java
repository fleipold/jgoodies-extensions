import org.buildobjects.BuildBase;
import org.buildobjects.BuildEnvironment;
import org.buildobjects.artifacts.Classes;
import org.buildobjects.artifacts.FileLocation;
import org.buildobjects.artifacts.Location;
import org.buildobjects.projectmodel.Libraries;
import org.buildobjects.publishers.*;
import org.buildobjects.tasks.JUnit;
import org.buildobjects.tasks.JavaC;
import static org.buildobjects.util.MixedUtils.combine;
import org.apache.commons.io.FileUtils;

import java.io.File;

/* Functional definition of build
*   Everything is passed into the constructor, hence no cycles!
*  But a bit of a smell with all those names moduleACompileTests etcpp*/
public class BuildFile extends BuildBase {
    private PublishableCombiner publisher;
    private Publishable fileoutlibs;

    public BuildFile() {}

    public BuildFile(BuildEnvironment environment) {
        super(environment);
    }

   

    private void initialize() {
        Location location = new FileLocation("src");

        Libraries libraries = new Libraries(tasks);


        Classes libs = combine(
                libraries.jgoodies.binding(),
                libraries.jgoodies.forms(),
                libraries.commons.lang()
        );

        Classes testLibs = libraries.junit();

        fileoutlibs = new AbsolutePathPublishable(new JarFileOutBuild(libs, "dependencies.jar"),
                new File("lib"));



        JavaC compileLib = tasks.javac(libs, location.src("java"));
        JavaC compileExamples = tasks.javac(compileLib.outputDep(), location.src("examples"));
        JavaC compileTests = tasks.javac(combine(compileExamples.outputDep(), testLibs), location.src("test"));
        JUnit runTests = tasks.junit(compileTests.outputDep(), location.src("test"));

        publisher = new PublishableCombiner(
              location.getName(),
                new JarFileOutBuild(compileLib.output(), "jgoodies-extensions.jar"),
                    new TestResultFileOut(runTests.results(),"test-results.txt")
             );


    }


    public void build() {
        initialize();
        build.publish(publisher);

    }


    public void fetch(){
        initialize();
        build.publish(fileoutlibs);

    }

    public static void main(String[] args) {
        new BuildFile(new BuildEnvironment(new File("target/manual-builds"))).build();
    }
}