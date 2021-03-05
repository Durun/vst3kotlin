# vst3kotlin
A VST3 SDK binding in Kotlin

Sorry, this README is under construction.

## For Users
### notice
ビルド時にOutOfMemoryErrorが出ることがありますが、もう1度ビルドすると成功することがあります。

### References
`gradlew dokkaHtml`を実行するとこのライブラリのリファレンスが`hosting/build/dokka`以下に生成されます。

## For Developpers (me)
### Project dependencies
- `hosting`
    - `vst3pluginterface`
        - `vst3cwrapper`
        - `util`
    - `window`
      - `util`
### Run Test
`gradlew downloadAll`を実行するとテスト用プラグインが用意されます。