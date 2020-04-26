package org.jarToDep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

public class JarToMVNDependency {

	public void navigateAndConvertJarToDependency(String absolutePath) {
		File root = new File(absolutePath);
		File[] files = root.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});
		if (files == null)
			return;
		for (File f : files) {
			if (f.isDirectory()) {
				navigateAndConvertJarToDependency(f.getAbsolutePath());
			} else {
				this.installJarIntoMavenRepo(f.getAbsolutePath());
			}
		}
	}

	private void installJarIntoMavenRepo(String absolutePath) {
		System.out.print("installing file " + absolutePath + "....");
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=" + absolutePath);
		builder.redirectErrorStream(true);
		Process p = null;
		try {
			p = builder.start();

			try (InputStreamReader isr = new InputStreamReader(p.getInputStream());
					BufferedReader br = new BufferedReader(isr)) {
				String line = br.readLine();
				if (line.startsWith("'mvn' is not recognized")) {
					System.err.println(": [FAILED]  'mvn' is not recognized, please install maven and try");
					return;
				}
				while ((line = br.readLine()) != null) {
					System.out.print(".");
					if (line.equals("[INFO] BUILD FAILURE")) {
						System.err.println(": [FAILED]  please try to install it manually.");
						return;
					}
				}
				System.out.println(": [SUCCESS]");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			p.destroy();
		}
		System.err.println(": [FAILED]  please try to install it manually.");
	}

	public static void main(String[] args) {
		JarToMVNDependency jarToDep = new JarToMVNDependency();
		if (args.length > 0) {
			File f = new File(args[0]);
			if (f.isDirectory()) {
				jarToDep.navigateAndConvertJarToDependency(f.getAbsolutePath());
			} else if (f.getName().toLowerCase().endsWith(".jar")) {
				jarToDep.installJarIntoMavenRepo(f.getAbsolutePath());
			}
		} else {
			System.err.println("[Error]  please provide valid file/folder path");
		}

	}
}
