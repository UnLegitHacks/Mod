# UnLegit 3.0
**is a free Minecraft Fabric hacked client.**
It can be used along with other mods such as Sodium.
## Setup
This uses Gradle. Here are steps to set it up:
1. Clone the repository using `git clone --recurse-submodules https://github.com/UnLegitHacks/Mod`.
2. CD into the local repository.
3. Run `./gradlew genSources`.
4. Open the folder as a Gradle project in your preferred IDE.
5. Run the client.
### Mixins
Mixins can be used to modify classes at runtime before they are loaded. UnLegit 3.0 uses it to inject its code into the
Minecraft client. This way, none of Mojang's copyrighted code is shipped. If you want to learn more about it, check out
its [documentation](https://docs.spongepowered.org/5.1.0/en/plugin/internals/mixins.html).
