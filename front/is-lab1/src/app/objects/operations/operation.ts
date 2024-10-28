export interface Operation {
    name: string;
    description: string;
    inputs: Array<{ label: string; name: string; type: string;  options: any[]}>;
    execute: any;
}