# NPSObfuscationMapper

You can deobfuscate VisualVM NPS snapshots of Minecraft with this tool.

```
Usage: java -jar NPSObfuscationMapper-0.0.jar MCPDIR NPS

Remaps the obfuscated method names in NPS to deobfuscated ones using the mappings in MCPDIR (which should contain methods.csv)
```
## Example
```
java -jar /path/to/NPSObfuscationMapper-0.0.jar /path/to/mcp-1.7.10-stable_12 my-snapshot.nps
```

# Building
Put all the jars in VisualVM's `modules` directory (`org-graalvm-visualvm-lib-profiler.jar` and so on) into a `libs` folder created in the root of this repo. You probably don't actually need all the JARs, but I didn't bother finding the set you actually need.
