# Yaml completion builder

Generate your bash completions from simple yaml

## Example

Given command `kat` with two subcommands, each with option. To generate completion create yaml with expected possibilities: 

```yaml
kat:
  - kat1 -k1
  - kat2 -k2
```

You may treat these instructions as recipe for directed acyclic graph. Same nodes are merged automatically. 

Then run:
```
java -jar target/completion-builder-1.0-SNAPSHOT.jar kat_completion.yml
```

This will generate file named `kat_completion` in `out` directory (or whatever directory you pass as second argument) with your completion.