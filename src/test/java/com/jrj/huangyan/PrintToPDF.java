/**
 * cdp4j Commercial License
 *
 * Copyright 2017, 2019 WebFolder OÜ
 *
 * Permission  is hereby  granted,  to "____" obtaining  a  copy of  this software  and
 * associated  documentation files  (the "Software"), to deal in  the Software  without
 * restriction, including without limitation  the rights  to use, copy, modify,  merge,
 * publish, distribute  and sublicense  of the Software,  and to permit persons to whom
 * the Software is furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  IMPLIED,
 * INCLUDING  BUT NOT  LIMITED  TO THE  WARRANTIES  OF  MERCHANTABILITY, FITNESS  FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS  OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jrj.huangyan;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.webfolder.cdp.AdaptiveProcessManager;
import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;

public class PrintToPDF {

	// Requires Headless Chrome
	// https://chromium.googlesource.com/chromium/src/+/lkgr/headless/README.md
	public static void main(String[] args) throws IOException {
		Launcher launcher = new Launcher();
		launcher.setProcessManager(new AdaptiveProcessManager());

		// Path file = createTempFile("cdp4j", ".pdf");

		Path file = Paths.get("/data/pdf/1/1.pdf");
		String url1 = "https://www.runoob.com/jqueryui/jqueryui-work.html";
		Path file2 = Paths.get("/data/pdf/1/2.pdf");
		String url2 = "https://www.runoob.com/jqueryui/jqueryui-intro.html";
		Path[] ps = new Path[] { file, file2 };
		String[] urls = new String[] { url1, url2 };
		try (SessionFactory factory = launcher.launch(asList("--disable-gpu", "--headless"))) {

			String context = factory.createBrowserContext();
			try (Session session = factory.create(context)) {
				for (int i = 0; i < ps.length; i++) {
					session.navigate(urls[i]);
					session.waitDocumentReady();

					byte[] content = session.getCommand().getPage().printToPDF();

					write(ps[i], content);
				}
			}

			factory.disposeBrowserContext(context);
		}

//		if (isDesktopSupported()) {
//			getDesktop().open(file.toFile());
//		}

		launcher.getProcessManager().kill();
	}
}
