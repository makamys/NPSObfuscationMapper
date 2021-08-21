# NPSObfuscationMapper

You can deobfuscate VisualVM NPS snapshots with this tool.

```
Usage: java -jar NPSObfuscationMapper-0.0.jar MCPDIR NPS

Remaps the obfuscated method names in NPS to deobfuscated ones using the mappings in MCPDIR (which should contain methods.csv)
```
## Example
```
java -jar /path/to/NPSObfuscationMapper-0.0.jar /path/to/mcp-1.7.10-stable_12 my-snapshot.nps
```
