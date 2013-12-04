WeatherPony Modification Loader (WPML)
================================================

WPML is an open-source, rapidly changing, Work In Progress (WIP) modification loader (ML) for Minecraft. Unlike other loaders, which aim to eliminating the need for edits, WPML exists for the sole purpose of loading such edits. It is for this reason that the 'M' in WPML stands for "Modification" rather than "Mod", which has been changing meaning within the Minecraft community to be more of a plugin.
The aim behind WPML is to allow mod makers to easily take advantage of ASM class transforming to improve compatibility between mods, without requiring in-depth knowledge of ASM. WPML offers multiple hooks in the loading process for programmers with deeper backgrounds in ASM/Java bytecode manipulation to control how edits are loaded, and it does not remove hooks provided by the Minecraft LaunchClassLoader.

WPML is constantly changing, as the code evolves towards the goal of merging base edits together.

WPML can be modified and distributed freely, but ownership belongs to the original creators, and only the original creators may earn revenue due to the loader. (We promise that the loader will always be available through an ad-free download.)

Pull Requests are not limited in the number of edited files, but no edits to Minecraft itself will be accepted. Pull Requests should follow the Sun/Oracle naming convention, except for constants, which should not be in all-caps.
Pull Requests are not required to be well-tested. Pull Requests that are obviously not tested well will most likely be rewritten before being added, if approved. Any Pull Request may be rewritten before being added.
