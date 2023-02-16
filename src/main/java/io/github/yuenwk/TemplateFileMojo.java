package io.github.yuenwk;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@Mojo(name = "generate")
public class TemplateFileMojo extends AbstractMojo {

    /**
     * Scope to filter the dependencies.
     */
    @Parameter(property = "scope")
    String scope;

    @Parameter(property = "fromTemplate")
    private File fromTemplate;

    @Parameter(property = "toFile")
    private File toFile;

    @Parameter
    private Map<String, String> templateValues;

    /**
     * Gives access to the Maven project information.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("====> Generate " + toFile.getName() + " from " + fromTemplate.getName() + " template start ... ");

        try {
            templateValues.putIfAbsent("version", project.getVersion());

            String template = FileUtils.readFileToString(fromTemplate, Charset.defaultCharset());

            StringSubstitutor sub = new StringSubstitutor(templateValues);
            String content = sub.replace(template);

            FileUtils.writeStringToFile(toFile, content, Charset.defaultCharset());

            getLog().info(toFile.getName() + " <=== Generate successfully !");
        } catch (IOException e) {
            getLog().error(toFile.getName() + " <=== Generation failure !");
            throw new RuntimeException(e);
        }
    }

}
