/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.dfki.kiara.example;

import java.util.ArrayList;
import java.util.List;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;

/**
 * Created by Dmitri Rubinstein on 9/11/14.
 */
public class HumanSimClient {

    public static enum LocationType {
        STATIC(0), DYNAMIC(1), AGENT(2);

        private final int value;
        private LocationType(int value) {
            this.value = value;
        }
    }

    public static class KeyValuePair {
        private String key;
        private String value;

        public KeyValuePair() {

        }

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class CustomParameter {
        public List<KeyValuePair> parameter;

        CustomParameter() { }

        CustomParameter(List<KeyValuePair> parameter) {
            this.parameter = parameter;
        }
    }

    public static interface HumanSim {

        public int createLocation(String name, float x, float y, float z,
                float locationReachedDistance,
                float preferredAvatarOrientation,
                LocationType type);

        public int createIntelligentAvatar(String avatarName,
                String implementationType,
                float x,
                float y,
                float z,
                CustomParameter customParameter);

        public void setNavMeshParameters(float avatarHeight, float avatarMaxClimb,
                float avatarRadius, float speed);

    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:21002/service";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection(uri)) {

            MethodBinding<HumanSim> binder
            = new MethodBinding<>(HumanSim.class)
            .bind("HumanSim.createLocation", "createLocation")
            .bind("HumanSim.createIntelligentAvatar", "createIntelligentAvatar");

            HumanSim humanSim = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) humanSim;
            Connection c = ri.getConnection();

            {
                int locationId = humanSim.createLocation("foobar", 0, 1, 0, 2, 1, LocationType.AGENT);
                System.out.format("humanSim.createLocation: result = %d%n", locationId);
            }

            {
                List<KeyValuePair> parameters = new ArrayList<>();
                parameters.add(new KeyValuePair("KEY", "VALUE"));
                int avatarId = humanSim.createIntelligentAvatar("myavatar", "impl", 1, 0, 1, new CustomParameter(parameters));
                System.out.format("humanSim.createIntelligentAvatar: result = %d%n", avatarId);
            }

            {
                humanSim.setNavMeshParameters(1, 2, 3, 4);
                System.out.format("humanSim.setNavMeshParameters: done%n");
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }


}
