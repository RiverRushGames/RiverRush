package nl.tudelft.ti2806.monkeyrush.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import nl.tudelft.ti2806.monkeyrush.Main;

/**
 * Main entrypoint for the html client.
 */
public class HtmlLauncher extends GwtApplication {

        /**
         * No idea.. Silencing checkstyle.
         * @return A GwtApplicationConfiguration.
         */
        @Override
        public GwtApplicationConfiguration getConfig() {
            final int width = 480;
            final int height = 320;
            return new GwtApplicationConfiguration(width, height);
        }

        /**
         * No idea.. Silencing checkstyle.
         * @return A listener for the application.
         */
        @Override
        public ApplicationListener getApplicationListener() {
            return new Main();
        }
}

